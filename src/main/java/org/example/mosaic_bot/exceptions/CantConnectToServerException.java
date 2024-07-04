package org.example.mosaic_bot.exceptions;

public class CantConnectToServerException extends RuntimeException{
    public CantConnectToServerException(String url){
        super("Can`t connect to %s".formatted(url));
    }
}
