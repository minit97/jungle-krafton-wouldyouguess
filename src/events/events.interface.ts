
// room_create, room_join, room_exit
export interface LobbyRequest {
    roomId: number;
    userId: number;
}

export interface Lobby {
    roomId: number;
    userList: number[];
    loadCnt: number;
}


// game_start, game_round_change, game_end
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

// drawer_draw_start, drawer_draw_move
export interface DrawerRequest {
    tool: string;
    xAxis: number;
    yAxis: number;
    color: string;
    size: number;
    fillColor: string;
    roomId: number;
}

// watcher_draw_start, watcher_draw_move
export interface LaserRequest {
    xAxis: number;
    yAxis: number;
    time: number;
    roomId: number;
}
