package org.example;

import java.util.concurrent.CompletableFuture;

public class MultiApiDataFetcher {
    public static void main(String[] args) {
        MultiApiDataFetcher multiApiDataFetcher=new MultiApiDataFetcher();
        CompletableFuture<String> whetherCompletableFuture=multiApiDataFetcher.getWeatherDetails();
        CompletableFuture<String> newsCompletableFuture=multiApiDataFetcher.getNewsDetails();
        CompletableFuture<String> scorerCompletableFuture=multiApiDataFetcher.getScoreDetails();
        CompletableFuture<Void> combinedCompletableFuture=CompletableFuture.allOf(whetherCompletableFuture,newsCompletableFuture,scorerCompletableFuture);
        combinedCompletableFuture.thenRun(()->{
            String whether= whetherCompletableFuture.join();
            String score=     scorerCompletableFuture.join();
            String news=     newsCompletableFuture.join();
            System.out.println(whether);
            System.out.println(score);
            System.out.println(news);
    }).join();
    }

    private CompletableFuture<String> getScoreDetails() {
        return CompletableFuture.supplyAsync(()->{
            simulateDelay(2000);
            return "Score : 300";});
    }

    private CompletableFuture<String> getNewsDetails() {
        return CompletableFuture.supplyAsync(()->{
            simulateDelay(1000);
            return "Java 23 released on 5th Sep 2024";});
    }

    private CompletableFuture<String> getWeatherDetails() {
        return CompletableFuture.supplyAsync(()->{
            simulateDelay(3000);
            return "Temp : 20C";});
    }

    private static void simulateDelay(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
