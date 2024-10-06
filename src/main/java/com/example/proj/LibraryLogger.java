package com.example.proj;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LibraryLogger {
    private String filePath;

    public void updateLendLog(String filePath, String id, Date date, String type) throws ParseException {
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

                        if (daysBetween > 15) {
                            System.out.println("Warning: The return date exceeds the allowable 15 days limit.");
                        } else {
                            lines.remove(entry);
                        }
                        break;
                    }
                }
                break;

            case "RENEW":
                for (int i = 0; i < lines.size(); i++) {
                    String entry = lines.get(i);
                    String[] parts = entry.split(",");
                    if (parts[0].equals(id)) {
                        found = true;
                        Date borrowedDate = new SimpleDateFormat("yyyy-MM-dd").parse(parts[1]);
                        long daysBetween = (date.getTime() - borrowedDate.getTime()) / (1000 * 60 * 60 * 24);

                        if (daysBetween < 15) {
                            lines.set(i, id + "," + new SimpleDateFormat("yyyy-MM-dd").format(date));
                        } else {
                            System.out.println("Warning: The renewal date exceeds the allowable 15 days limit.");
                        }
                        break;
                    }
                }
                break;

            default:
                System.out.println("Invalid type. Use 'LEND', 'RETURN', or 'RENEW'.");
                return;
        }

        // Ghi lại các dòng đã cập nhật vào file
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
                String lendLogFilePath = "src/main/resources/database/" + memberId + "Lend.txt";
                String reserveLogFilePath = "src/main/resources/database/" + memberId + "Reserve.txt";

                File lendLogFile = new File(lendLogFilePath);
                if (!lendLogFile.exists()) {
                    lendLogFile.createNewFile();
                    System.out.println("Log file created for member (Lend): " + memberId);
                } else {
                    System.out.println("Log file loaded for member (Lend): " + memberId);
                }

                File reserveLogFile = new File(reserveLogFilePath);
                if (!reserveLogFile.exists()) {
                    reserveLogFile.createNewFile();
                    System.out.println("Log file created for member (Reserve): " + memberId);
                } else {
                    System.out.println("Log file loaded for member (Reserve): " + memberId);
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
