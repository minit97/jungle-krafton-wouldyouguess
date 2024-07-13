import {Controller, Post, Body, Res, Req} from '@nestjs/common';
import {LivekitService} from './livekit.service';
import {WebhookReceiver} from 'livekit-server-sdk';
import {Request, Response} from 'express';
import * as dotenv from 'dotenv';
import * as process from 'process';

dotenv.config();

@Controller('')
export class LivekitController {
    private readonly webhookReceiver: WebhookReceiver; // WebhookReceiver 인스턴스 선언

    constructor(private readonly livekitService: LivekitService) {
        const LIVEKIT_API_KEY = process.env.LIVEKIT_API_KEY || "devkey";
        const LIVEKIT_API_SECRET = process.env.LIVEKIT_API_SECRET || "secret";

        // WebhookReceiver 인스턴스 생성
        this.webhookReceiver = new WebhookReceiver(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
    }

    @Post('token')
    async createToken(@Body() body: { roomName: string, participantName: string }, @Res() res: Response) {
        console.log("토큰 발급 요청 받음");
        const token = await this.livekitService.createToken(body.roomName, body.participantName);
        res.json({token});
    }

    @Post('livekit/ webhook')
    async handleWebhook(@Req() req: Request, @Res() res: Response) {
        try {
            const event = await this.webhookReceiver.receive(
                req.body,
                req.headers.authorization
            );
            console.log('Received webhook event:', event);

            // 웹훅 이벤트 처리 로직 구현 (예: 이벤트 종류에 따라 다른 작업 수행)

        } catch (error) {
            console.error('Error validating webhook event:', error);
            res.status(400).send();
        }

        res.status(200).send();
    }
}

