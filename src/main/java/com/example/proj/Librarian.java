package com.example.proj;

import java.io.*;
import java.util.*;

public class Librarian extends Account{

    private Map<String, Member> memberMap;
    private String filePath;

    public Librarian() {
        super("admin", AccountStatus.ACTIVE, "admin");
        this.getCatalog().ImportFromFile();
        File file = new File("src/main/resources/database/members.txt");
        if (file.exists()) {
            String absolute = file.getAbsolutePath();
            this.filePath = absolute;
        }
        this.memberMap = new HashMap<>();
        this.loadMemberFromFile();
    }

    public Librarian(String id, AccountStatus status, String password) {
        super(id, status, password);
        this.getCatalog().ImportFromFile();
        File file = new File("src/main/resources/database/members.txt");
        if (file.exists()) {
            String absolute = file.getAbsolutePath();
            this.filePath = absolute;
        }
        this.memberMap = new HashMap<>();
        this.loadMemberFromFile();
    }

    public Map<String,Member> getMemberMap() {
        return this.memberMap;
    }


    private void putMemberInMap(Member member) {
        this.memberMap.put(member.getId(), member);
    }



    private void loadMemberFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            //System.out.println("yay");
            String line;
            while ((line = br.readLine()) != null) {
                String[] tmp = line.split(";");
                if (tmp.length != 5) {
                    continue;
                }
                String id = tmp[0].trim();
                String accountStatus = tmp[1].trim();
                String password = tmp[2].trim();
                int totalBooksCheckedOut = Integer.parseInt(tmp[3].trim());
                int point = Integer.parseInt(tmp[4].trim());

                Member member = new Member(id, AccountStatus.valueOf(accountStatus.toUpperCase()), password, totalBooksCheckedOut, point);

                this.putMemberInMap(member);


            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addBookItem(BookItem bookItem) {
        this.getCatalog().addBookItem(bookItem);
        this.getCatalog().writeBookItemToFile(bookItem);
    }

    public void blockMember(String id) {
        // lines container.
        List<String> lines = new ArrayList<>();
        boolean memberFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields[0].equals(id)) {
                    fields[1] = "BLACKLISTED";
                    memberFound = true;
                    //change in that time.
                    this.getMemberMap().get(fields[0]).setStatus(AccountStatus.BLACKLISTED);
                }
                //put every line in the list.
                lines.add(String.join(";", fields));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // write to a new file
        if (memberFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Member " + id + " has been blocked.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Member with ID " + id + " not found.");
        }
    }

    public void reducePointMember(String id) {
        // lines container.
        List<String> lines = new ArrayList<>();
        boolean memberFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields[0].equals(id)) {
                    fields[4] = String.valueOf(Integer.parseInt(fields[4]) - 1);
                    memberFound = true;

                    //change in that time.
                    int point = this.getMemberMap().get(fields[0]).getPoint() - 1;
                    this.getMemberMap().get(fields[0]).setPoint(point);
                }
                //put every line in the list.
                lines.add(String.join(";", fields));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write to a new file
        if (memberFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Member " + id + " has lost 1 reputation.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Member with ID " + id + " not found.");
        }
    }

    public void addNewMember(Member member) {
        String filePath = "src/main/resources/database/members.txt";
        String memberData = member.getId() + ";" +
                member.getStatus() + ";" +
                member.getPassword() + ";" +
                member.getTotalBooksCheckedOut() + ";" +
                member.getPoint() + ";";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(memberData);
            writer.newLine();
            System.out.println("Member added: " + memberData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void increaseBookForMember(String id) {
        // lines container.
        List<String> lines = new ArrayList<>();
        boolean memberFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields[0].equals(id)) {
                    fields[3] = String.valueOf(Integer.parseInt(fields[3]) + 1);
                    memberFound = true;

                    //change in that time.
                    int nob = this.getMemberMap().get(fields[0]).getTotalBooksCheckedOut() + 1;
                    this.getMemberMap().get(fields[0]).setTotalBooksCheckedOut(nob);
                }
                //put every line in the list.
                lines.add(String.join(";", fields));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write to a new file
        if (memberFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Member " + id + " has borrowed 1 book.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Member with ID " + id + " not found.");
        }
    }

    public void decreaseBookForMember(String id) {
        // lines container.
        List<String> lines = new ArrayList<>();
        boolean memberFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields[0].equals(id)) {
                    fields[3] = String.valueOf(Integer.parseInt(fields[3]) - 1);
                    memberFound = true;

                    //change in that time.
                    int nob = this.getMemberMap().get(fields[0]).getTotalBooksCheckedOut() - 1;
                    this.getMemberMap().get(fields[0]).setTotalBooksCheckedOut(nob);
                }
                //put every line in the list.
                lines.add(String.join(";", fields));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write to a new file
        if (memberFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
                System.out.println("Member " + id + " has returned 1 book.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Member with ID " + id + " not found.");
        }
    }

    public void removeBook(String id) {
        this.getCatalog().removeBookById(id);
    }

    public void printAllMember() {
        System.out.println(this.memberMap.size());
        for(String s : this.memberMap.keySet()) {
            this.memberMap.get(s).printInfo();
        }
    }
}
