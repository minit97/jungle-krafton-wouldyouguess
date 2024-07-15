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
  GameEndRequest,
  GameRoundChangeRequest,
  GameStartRequest,
  Lobby,
  LobbyRequest
} from "./events.interface";


@WebSocketGateway({ cors: { origin : '*' } })
export class EventsGateway implements OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect {
  @WebSocketServer()
  private server;

  private logger = new Logger("socket-events");
  private lobbies: Lobby[] = [];


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
    // this.logger.log(`room_create: ${JSON.stringify(request)}`);

    const { roomId, userId } = request;
    let lobby = this.lobbies.find(lobby => lobby.roomId === roomId);

    if (lobby) {
      !lobby.userList.includes(userId) && lobby.userList.push(userId);  // roomId가 존재하면 userId 추가 (중복 방지)
    } else {
      // roomId가 존재하지 않으면 새로운 Lobby 객체 생성하여 추가
      const newLobby: Lobby = {
        roomId: roomId,
        userList: [userId]
      };
      this.lobbies.push(newLobby);
      lobby = newLobby;
    }

    client.join(roomId.toString());   // 사용자를 roomId 방에 추가
  }

  @SubscribeMessage('room_join')
  handleRoomJoin(@MessageBody() request:LobbyRequest, @ConnectedSocket() client: Socket)  {
    // this.logger.log(`room_join: ${JSON.stringify(request)}`);

    const { roomId, userId } = request;

    const lobby = this.lobbies.find(lobby => lobby.roomId === roomId);
    if (!lobby) {
      this.logger.warn(`Room with ID ${roomId} does not exist.`);
    }

    !lobby.userList.includes(userId) && lobby.userList.push(userId);

    client.join(roomId.toString());                                 // 사용자를 roomId 방에 추가
    client.to(roomId.toString()).emit('room_join', roomId);    // 해당 방의 모든 사용자에게 메시지 전송
  }

  @SubscribeMessage('room_exit')
  handleRoomExit(@MessageBody() request:LobbyRequest, @ConnectedSocket() client: Socket) {
    // this.logger.log(`room_exit: ${JSON.stringify(request)}`);

    const { roomId, userId } = request;

    const lobby = this.lobbies.find(lobby => lobby.roomId === roomId);
    if (!lobby) {
      this.logger.warn(`Room with ID ${roomId} does not exist.`);
    }

    lobby.userList = lobby.userList.filter(id => id !== userId);
    client.leave(roomId.toString());
    client.to(roomId.toString()).emit('room_exit', request);  // 해당 방의 모든 사용자에게 메시지 전송
  }


  @SubscribeMessage('game_start')
  handleGameJoin(@MessageBody() request: GameStartRequest, @ConnectedSocket() client: Socket)  {
    this.logger.log(`game_start: ${JSON.stringify(request)}`);

    const { mode, userId, roomId, gameId } = request;

    const lobby = this.lobbies.find(lobby => lobby.roomId === roomId);
    if (!lobby) {
      this.logger.warn(`Room with ID ${roomId} does not exist.`);
    }

    client.to(roomId.toString()).emit('game_start', request);
  }

  @SubscribeMessage('game_round_change')
  handleGameTurnChange(@MessageBody() request: GameRoundChangeRequest, @ConnectedSocket() client: Socket)  {
    this.logger.log(`game_round_change: ${JSON.stringify(request)}`);

    const { userId, roomId, gameId, round } = request;

    const lobby = this.lobbies.find(lobby => lobby.roomId === roomId);
    if (!lobby) {
      this.logger.warn(`Room with ID ${roomId} does not exist.`);
    }

    client.to(roomId.toString()).emit('game_round_change', request);
  }

  @SubscribeMessage('game_end')
  handleGameEnd(@MessageBody() request: GameEndRequest, @ConnectedSocket() client: Socket) {
    this.logger.log(`game_end: ${JSON.stringify(request)}`);

    const { userId, roomId, gameId } = request;

    const lobby = this.lobbies.find(lobby => lobby.roomId === roomId);
    if (!lobby) {
      this.logger.warn(`Room with ID ${roomId} does not exist.`);
    }

    client.to(roomId.toString()).emit('game_end', request);
  }


  private temp = 0;
  @SubscribeMessage('game_result')
  handleGameResultAait(@MessageBody() request: GameRoundChangeRequest, @ConnectedSocket() client: Socket)  {

    const { userId, roomId, gameId, round } = request;

    const lobby = this.lobbies.find(lobby => lobby.roomId === roomId);
    if (!lobby) {
      this.logger.warn(`Room with ID ${roomId} does not exist.`);
    }

    this.temp += 1
    console.log("박현민 1 : ", lobby.userList.length);
    console.log("박현민 2 : ", this.temp);
    if (lobby.userList.length === this.temp) {
      client.to(roomId.toString()).emit('game_result', request);
    }
  }

  // drawer : 그림 그리기 / watcher : 그림 그리는 좌표 전송
  @SubscribeMessage('drawer_draw_start')
  handleDrawerDrawStart(@MessageBody() request: DrawerRequest,  @ConnectedSocket() client: Socket) {
    const { roomId } = request;

    const lobby = this.lobbies.find(lobby => lobby.roomId === roomId);
    if (!lobby) {
      this.logger.warn(`Room with ID ${roomId} does not exist.`);
    }

    client.to(roomId.toString()).emit('drawer_draw_start', request);
  }

  @SubscribeMessage('drawer_draw_move')
  handleDrawerDrawMove(@MessageBody() request: DrawerRequest,  @ConnectedSocket() client: Socket) {
    const { roomId } = request;

    const lobby = this.lobbies.find(lobby => lobby.roomId === roomId);
    if (!lobby) {
      this.logger.warn(`Room with ID ${roomId} does not exist.`);
    }

    client.to(roomId.toString()).emit('drawer_draw_move', request);
  }

  //todo 후작업 : watcher(socketId or userId 판별)들에게만 보이는 레이저 포인터 생성 - 클라에서 삭제 로직

  @SubscribeMessage('watcher_draw_start')
  handleWatcherDrawStart(@MessageBody() request: DrawerRequest,  @ConnectedSocket() client: Socket): void {

  }

  @SubscribeMessage('watcher_draw_move')
  handleWatcherDrawMove(@MessageBody() request: DrawerRequest,  @ConnectedSocket() client: Socket): void {

  }
}

