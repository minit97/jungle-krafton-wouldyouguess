import {
  ConnectedSocket,
  MessageBody,
  OnGatewayConnection,
  OnGatewayDisconnect,
  OnGatewayInit,
  SubscribeMessage,
  WebSocketGateway,
  WebSocketServer,
} from '@nestjs/websockets';
import {Server, Socket} from 'socket.io';
import {Logger} from "@nestjs/common";
import {
  DrawerRequest,
  GameEndRequest, GameLoadingRequest,
  GameRoundChangeRequest,
  GameStartRequest, GameVotingRequest, WatcherRequest,
  Lobby,
  LobbyRequest
} from "./events.interface";


@WebSocketGateway({ cors: { origin : '*' } })
export class EventsGateway implements OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect {
  @WebSocketServer()
  private server;

  private logger = new Logger("socket-events");
  private lobbies: Lobby[] = [];

  private findLobby(roomId: number): Lobby | undefined {
    const lobby = this.lobbies.find(lobby => lobby.roomId === roomId);
    if (!lobby) throw new Error(`Room with ID ${roomId} does not exist.`);
    return lobby;
  }

  afterInit(server: Server): any {
    this.logger.log("init");
  }

  handleConnection(@ConnectedSocket() socket: Socket) {
    this.logger.log(`socket connected: ${socket.id}`);
  }

  handleDisconnect(@ConnectedSocket() socket: Socket) {
    this.logger.log(`socket disconnected: ${socket.id}`);
  }

  @SubscribeMessage('room_create')
  handleRoomCreate(@MessageBody() request:LobbyRequest, @ConnectedSocket() client: Socket) {
    const { roomId, userId } = request;

    const lobby = this.lobbies.find(lobby => lobby.roomId === roomId);
    if (lobby) {
      !lobby.userList.includes(userId) && lobby.userList.push(userId);
    } else {
      const newLobby: Lobby = {
        roomId: roomId,
        userList: [userId],
        loadCnt: 0
      };
      this.lobbies.push(newLobby);
    }

    client.join(roomId.toString());
  }

  @SubscribeMessage('room_join')
  handleRoomJoin(@MessageBody() request:LobbyRequest, @ConnectedSocket() client: Socket)  {
    const { roomId, userId } = request;
    const lobby = this.findLobby(roomId);

    !lobby.userList.includes(userId) && lobby.userList.push(userId);

    client.join(roomId.toString());
    client.to(roomId.toString()).emit('room_join', roomId);
  }

  @SubscribeMessage('room_exit')
  handleRoomExit(@MessageBody() request:LobbyRequest, @ConnectedSocket() client: Socket) {
    const { roomId, userId } = request;
    const lobby = this.findLobby(roomId);

    lobby.userList = lobby.userList.filter(id => id !== userId);

    client.leave(roomId.toString());
    client.to(roomId.toString()).emit('room_exit', request);
  }

  @SubscribeMessage('game_start')
  handleGameJoin(@MessageBody() request: GameStartRequest, @ConnectedSocket() client: Socket)  {
    const { roomId} = request;
    this.findLobby(roomId);

    this.server.in(roomId.toString()).emit('game_start', request);
  }

  @SubscribeMessage('game_round_change')
  handleGameTurnChange(@MessageBody() request: GameRoundChangeRequest, @ConnectedSocket() client: Socket)  {
    const { roomId} = request;
    this.findLobby(roomId);

    this.server.in(roomId.toString()).emit('game_round_change', request);
  }

  @SubscribeMessage('game_end')
  handleGameEnd(@MessageBody() request: GameEndRequest, @ConnectedSocket() client: Socket) {
    const { roomId } = request;
    this.findLobby(roomId);

    this.server.in(roomId.toString()).emit('game_end', request);
  }

  @SubscribeMessage('drawer_draw_start')
  handleDrawerDrawStart(@MessageBody() request: DrawerRequest,  @ConnectedSocket() client: Socket) {
    const { roomId } = request;
    this.findLobby(roomId);

    client.to(roomId.toString()).emit('drawer_draw_start', request);
  }

  @SubscribeMessage('drawer_draw_move')
  handleDrawerDrawMove(@MessageBody() request: DrawerRequest,  @ConnectedSocket() client: Socket) {
    const { roomId } = request;
    this.findLobby(roomId);

    client.to(roomId.toString()).emit('drawer_draw_move', request);
  }

  @SubscribeMessage('watcher_draw_start')
  handleWatcherDrawStart(@MessageBody() request: WatcherRequest, @ConnectedSocket() client: Socket) {
    const { roomId } = request;
    this.findLobby(roomId);

    client.to(roomId.toString()).emit('watcher_draw_start', request);
  }

  @SubscribeMessage('watcher_draw_move')
  handleWatcherDrawMove(@MessageBody() request: WatcherRequest, @ConnectedSocket() client: Socket) {
    const { roomId } = request;
    this.findLobby(roomId);

    client.to(roomId.toString()).emit('watcher_draw_move', request);
  }

  @SubscribeMessage('game_voting')
  handleGameVoting(@MessageBody() request: GameVotingRequest,  @ConnectedSocket() client: Socket) {
    const { roomId } = request;
    this.findLobby(roomId);

    this.server.in(roomId.toString()).emit('game_voting', request);
  }

  @SubscribeMessage('game_loading')
  handleGameResultAait(@MessageBody() request: GameLoadingRequest, @ConnectedSocket() client: Socket)  {
    const { roomId } = request;
    const lobby = this.findLobby(roomId);

    lobby.loadCnt += 1
    if (lobby.userList.length === lobby.loadCnt) {
      lobby.loadCnt = 0
      this.server.in(roomId.toString()).emit('game_loading', request);
    }
  }

}

