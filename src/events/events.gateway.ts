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
import {PointData} from "./events.interface";


@WebSocketGateway({ cors: { origin : '*' } })
export class EventsGateway implements OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect {
  @WebSocketServer()
  private server;

  private logger = new Logger("socket-events");

  connectedClients: { [socketId: string]: boolean } = {};
  clientNickname: { [socketId: string]: string } = {};
  roomUsers: { [key: string]: string[] } = {};

  afterInit(server: Server): any {
    this.logger.log("init");
  }

  handleConnection(@ConnectedSocket() socket: Socket) {
    this.logger.log(`socket connected: ${socket.id}`);
  }

  // disconnect 되면 강퇴해야지 않을까?
  handleDisconnect(@ConnectedSocket() socket: Socket) {
    this.logger.log(`socket disconnected: ${socket.id}`);
  }

  // connection 후 콜백 함수
  @SubscribeMessage('connenct_check')
  handleConnectCheck(@MessageBody() message:string) {
    this.logger.log(message);
  }


  // game 생성 -> Room에 따른 drawer, watcher 결정
  @SubscribeMessage('game_join')
  handleGameJoin(@MessageBody() message:string)  {
    this.logger.log(message);
  }

  // game turn 종료 시 -> drawer, watcher 변경 -> api에서 와야 하나?
  @SubscribeMessage('game_turn_change')
  handleGameTurnChange(@MessageBody() message:string) {
    this.logger.log(message);
  }

  @SubscribeMessage('game_end')
  handleGameEnd(@MessageBody() message:string) {
    this.logger.log(message);
  }

  // drawer : 그림 그리기 / watcher : 그림 그리는 좌표 전송
  @SubscribeMessage('drawer_draw_start')
  handleDrawerDrawStart(@MessageBody() data: PointData[],  @ConnectedSocket() client: Socket): void {
    // 여기서 받은 좌표를 처리합니다.
    client.broadcast.emit('start_coordinates1', data);
  }

  @SubscribeMessage('drawer_draw_move')
  handleDrawerDrawMove(@MessageBody() data: PointData[],  @ConnectedSocket() client: Socket): void {
    // 여기서 받은 좌표를 처리합니다.
    client.broadcast.emit('coordinates1', data);
  }

  //todo 후작업 : watcher(socketId or userId 판별)들에게만 보이는 레이저 포인터 생성 - 클라에서 삭제 로직

  // @SubscribeMessage('watcher_draw_start')
  // handleWatcherDrawStart(@MessageBody() data: PointData[],  @ConnectedSocket() client: Socket): void {
  //
  // }
  //
  // @SubscribeMessage('watcher_draw_move')
  // handleWatcherDrawMove(@MessageBody() data: PointData[],  @ConnectedSocket() client: Socket): void {
  //
  // }
}