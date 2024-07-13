import {MiddlewareConsumer, Module, NestModule} from '@nestjs/common';
import {ConfigModule, ConfigService} from "@nestjs/config";
import {AppController} from './app.controller';
import {AppService} from './app.service';
import {LoggerMiddleware} from "./middlewares/logger.middlewares";
import {EventsModule} from './events/events.module';
import {LivekitModule} from "./livekit/livekit.module";

@Module({
  imports: [ConfigModule.forRoot({ isGlobal: true }), EventsModule, LivekitModule],
  controllers: [AppController],
  providers: [AppService, ConfigService],
})
export class AppModule implements NestModule {
  configure(consumer: MiddlewareConsumer): any {
    consumer.apply(LoggerMiddleware).forRoutes('*');
  }
}
