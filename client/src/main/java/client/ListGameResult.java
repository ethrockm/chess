package client;

import service.GameService;

import java.util.Collection;

public record ListGameResult(Collection<GameService.GameInfo> games){}
