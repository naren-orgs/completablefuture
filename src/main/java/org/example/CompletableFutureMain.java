package org.example;


import org.example.async.database.EmployeeHelper;
import org.example.async.database.dto.Employee;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CompletableFutureMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFutureMain completableFutureMain=new CompletableFutureMain();

        CompletableFuture<Integer> thenComposeResults =
                completableFutureMain.getEmployeeDetails()
                .thenCompose(completableFutureMain::getRating)
                ;
        System.out.println("Rating=="+thenComposeResults.get());

         CompletableFuture<String> combinedResult=getCountByGender().thenCombine(getListOfEmails(),(empMap,emails)->empMap + " "+ emails);
        System.out.println("Results==="+combinedResult.get());

    }

    private static CompletableFuture<List<String>> getListOfEmails() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("List of Emails Thread :" + Thread.currentThread().getName());
            return Objects.requireNonNull(EmployeeHelper.fetchEmployees()).stream()
                    .map(Employee::getEmail)
                    .collect(Collectors.toList());
        });
    }

    private static CompletableFuture<Map<String,Long>> getCountByGender() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Count By Gender : "+Thread.currentThread().getName());
            return Objects.requireNonNull(EmployeeHelper.fetchEmployees()).stream()
                    .collect(Collectors.groupingBy(Employee::getGender,
                            Collectors.counting()));
        });
    }

    private CompletableFuture<Integer> getRating(Employee employee) {
        return CompletableFuture.supplyAsync(employee::getRating);
    }

    private CompletableFuture<Employee> getEmployeeDetails() {
       return  CompletableFuture.supplyAsync(()->{
            return Objects.requireNonNull(EmployeeHelper.fetchEmployees())
                    .stream()
                    .filter(e->"96-403-2944".equals(e.getEmployeeId()))
                    .findAny()
                    .orElse(null);
    });
    }

}