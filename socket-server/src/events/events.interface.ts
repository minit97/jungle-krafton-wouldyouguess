export interface LobbyRequest {
    roomId: number;
    userId: number;
}

export interface Lobby {
    roomId: number;
    userList: number[];
    loadCnt: number;
}

export interface GameStartRequest {
    mode: number;
    userId: number;
    roomId: number;
    gameId: number;
}

export interface GameRoundChangeRequest {
    userId: number;
    roomId: number;
    gameId: number;
    round: number;
}

export interface GameEndRequest {
    userId: number;
    roomId: number;
    gameId: number;
}

export interface GameLoadingRequest {
    roomId: number;
    nextPageUrl: string;
}

export interface GameVotingRequest {
    roomId: number;
    userId: number;
    votingUserId: number;
    previousVotingUserId:number;
}

export interface DrawerRequest {
    tool: string;
    xAxis: number;
    yAxis: number;
    color: string;
    size: number;
    fillColor: string;
    roomId: number;
}

export interface WatcherRequest {
    xAxis: number;
    yAxis: number;
    time: number;
    roomId: number;
}
