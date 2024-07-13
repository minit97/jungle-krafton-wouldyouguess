import {NestFactory} from '@nestjs/core';
import {AppModule} from './app.module';
import {LivekitModule} from './livekit/livekit.module';
import * as process from 'process';

declare const module: any;

async function bootstrap() {
    const app = await NestFactory.create(AppModule);
    const livekitApp = await NestFactory.create(LivekitModule);

    // CORS 설정 (main.ts에서 한 번만 설정)
    app.enableCors({
        origin: '*',
        methods: ['GET', 'POST', 'OPTIONS'],
        allowedHeaders: ['Content-Type', 'Authorization'],
    });

    livekitApp.enableCors({
        origin: '*',
        methods: ['GET', 'POST', 'OPTIONS'],
        allowedHeaders: ['Content-Type', 'Authorization'],
    });

    // 각각 다른 포트에서 실행
    const port = process.env.PORT || 3000;
    await app.listen(port);
    await livekitApp.listen(6080);

    console.log(`Application server listening on port ${port}`);
    console.log(`LiveKit server listening on port 6080`);

    if (module.hot) {
        module.hot.accept();
        module.hot.dispose(() => app.close());
    }
}

bootstrap();
