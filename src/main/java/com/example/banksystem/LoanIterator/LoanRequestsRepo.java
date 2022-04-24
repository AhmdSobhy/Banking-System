package com.example.banksystem.LoanIterator;

import com.example.banksystem.Loan.EducationalLoan;
import com.example.banksystem.Loan.HomeLoan;
import com.example.banksystem.Loan.Loan;
import com.example.banksystem.Loan.PersonalLoan;
import com.example.dataBase.Functions.DataBaseReader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanRequestsRepo implements  Container{

    //i need a function in database class to read all loan requests
    public LoanRequest [] requests =null; //DataBase.getLoanRequests();
    private Iterator LoanIter;

    public List<LoanRequest> getAllLoanRequests() throws SQLException {
        List<LoanRequest> loanRequestList=new ArrayList<>();
        DataBaseReader dataBaseReader=new DataBaseReader();
        int loanSize;
        String query="select * from loan";
        ResultSet resultSet=dataBaseReader.read(query);
        Loan loan;
        String status;
        String loanType;
        int userId;
        //Note: the way of assigning values to object from database I search for a better way than it
        while (resultSet.next()){

            status=resultSet.getString("status");
            userId=resultSet.getInt("user_id");
            loanType=resultSet.getString("loan_type");
            if(loanType.compareTo("e")==0){
                loan=new EducationalLoan();
            }
            else if(loanType.compareTo("h")==0){
                loan=new HomeLoan();
            }
            else loan=new PersonalLoan();
            loan.setAmount(resultSet.getDouble("amount"));
            loan.setNumOfMonths(resultSet.getInt("nOfMonth"));

            loanRequestList.add(new LoanRequest(loan,status,userId));
        }
        dataBaseReader.closeConnection();
        return loanRequestList;
    }
    @Override
    public Iterator getIterator() {
        LoanIter = new LoanRequestsIterator(requests);
        return LoanIter;
    }

    //test getAllLoanRequests
    public static void main(String []args) throws SQLException {
        LoanRequestsRepo loanRequestsRepo=new LoanRequestsRepo();
        List<LoanRequest>list=loanRequestsRepo.getAllLoanRequests();

        for (LoanRequest loanRequest:list) {
            System.out.println("Status: "+loanRequest.getStatus());
            System.out.println("User id: "+loanRequest.getUserID());
        }
    }
}
