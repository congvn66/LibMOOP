package com.example.proj;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LibraryLogger {
    private String filePath;

    public void updateLog(Member member, String id, Date date, String type) throws ParseException {
        String filePath = "src/main/resources/database/" + member.getId() + ".txt";
        File logFile = new File(filePath);
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (type.toUpperCase()) {
            case "LEND":
//                if (member.getStatus() == AccountStatus.BLACKLISTED) {
//                    System.out.println("You've been blocked for returning books late too many times");
//                    break;
//                }
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                String newEntry = id + "," + formattedDate;
                lines.add(newEntry);
                break;

            case "RETURN":
                for (String entry : lines) {
                    String[] parts = entry.split(",");
                    if (parts[0].equals(id)) {
                        found = true;
                        Date borrowedDate = new SimpleDateFormat("yyyy-MM-dd").parse(parts[1]);
                        long daysBetween = (date.getTime() - borrowedDate.getTime()) / (1000 * 60 * 60 * 24);
                        Librarian admin = new Librarian();
                        admin.decreaseBookForMember(member.getId());
                        member.setTotalBooksCheckedOut(member.getTotalBooksCheckedOut() - 1);
                        member.updateBook(id, 13, "AVAILABLE");
                        if (daysBetween > 15) {
                            System.out.println("Warning: The return date exceeds the allowable 15 days limit!");
                            int p = member.getPoint() - 1;
                            member.setPoint(p);
                            admin.reducePointMember(member.getId());
                            if (member.getPoint() == 0) {
                                member.setStatus(AccountStatus.BLACKLISTED);
                                admin.blockMember(member.getId());
                            }
                        }
                        lines.remove(entry);
                        break;
                    }
                }
                if (!found) {
                    System.out.println("You haven't borrowed this book.");
                }
                break;

            case "RENEW":
                if (member.getStatus() == AccountStatus.BLACKLISTED) {
                    System.out.println("You've been blocked for returning books late too many times");
                    break;
                }
                for (int i = 0; i < lines.size(); i++) {
                    String entry = lines.get(i);
                    String[] parts = entry.split(",");
                    if (parts[0].equals(id)) {
                        found = true;
                        Date borrowedDate = new SimpleDateFormat("yyyy-MM-dd").parse(parts[1]);
                        long daysBetween = (date.getTime() - borrowedDate.getTime()) / (1000 * 60 * 60 * 24);

                        lines.set(i, id + "," + new SimpleDateFormat("yyyy-MM-dd").format(date));
                        if (daysBetween > 15) {
                            System.out.println("Warning: The renewal date exceeds the allowable 15 days limit.");
                            Librarian admin = new Librarian();
                            int p = member.getPoint() - 1;
                            member.setPoint(p);
                            admin.reducePointMember(member.getId());
                            if (member.getPoint() == 0) {
                                member.setStatus(AccountStatus.BLACKLISTED);
                                admin.blockMember(member.getId());
                            }
                        }
                        break;
                    }
                }
                //break;
                if (!found) {
                    System.out.println("You haven't borrowed this book.");
                }
                break;

            default:
                System.out.println("Invalid type. Use 'LEND', 'RETURN', or 'RENEW'.");
                return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile))) {
            for (String entry : lines) {
                writer.write(entry);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void generateMemberLog(String membersFilePath, String memberId) {
        BufferedReader reader = null;
        boolean memberExists = false;
        try {
            reader = new BufferedReader(new FileReader(membersFilePath));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] memberDetails = line.split(";");

                if (memberDetails[0].equals(memberId)) {
                    memberExists = true;
                    break;
                }
            }

            if (memberExists) {
                String memberLogFilePath = "src/main/resources/database/" + memberId + ".txt";

                File memberLogFile = new File(memberLogFilePath);
                if (!memberLogFile.exists()) {
                    memberLogFile.createNewFile();
                    System.out.println("Log file created for member: " + memberId);
                } else {
                    System.out.println("Log file loaded for member: " + memberId);
                }
            } else {
                System.out.println("Member " + memberId + " does not exist in the member list.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
