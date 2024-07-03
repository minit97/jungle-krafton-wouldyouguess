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

interface PointData {
  tool: string;
  points: number[];
}

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

  handleDisconnect(@ConnectedSocket() socket: Socket) {
    this.logger.log(`socket disconnected: ${socket.id}`);
  }

  @SubscribeMessage('connenct_check')
  handleConnectCheck(@MessageBody() message:string) {
    this.logger.log(message);
  }


  @SubscribeMessage('message')
  handleMessage(@MessageBody() message:string): void {
    this.logger.log(message);
    this.server.emit('chat', message);
  }


  @SubscribeMessage('start_coordinates')
  handleStartCoordinates(@MessageBody() data: PointData[],  @ConnectedSocket() client: Socket): void {
    // console.log(`Received coordinates: ${data}`);
    // 여기서 받은 좌표를 처리합니다.
    client.broadcast.emit('start_coordinates1', data);
  }



  @SubscribeMessage('coordinates')
  handleCoordinates(@MessageBody() data: PointData[],  @ConnectedSocket() client: Socket): void {
    console.log(`Received coordinates: ${data}`);
    // 여기서 받은 좌표를 처리합니다.
    client.broadcast.emit('coordinates1', data);
  }


}