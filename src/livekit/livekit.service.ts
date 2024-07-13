import {Injectable, HttpException, HttpStatus, Req, Res} from '@nestjs/common';
import {AccessToken, WebhookReceiver} from 'livekit-server-sdk';
import * as dotenv from 'dotenv';
import {Request, Response} from 'express';
import * as process from 'process';

dotenv.config();

const LIVEKIT_API_KEY = process.env.LIVEKIT_API_KEY;
const LIVEKIT_API_SECRET = process.env.LIVEKIT_API_SECRET;

@Injectable()
export class LivekitService {
    private webhookReceiver: WebhookReceiver;

    constructor() {
        const LIVEKIT_API_KEY = process.env.LIVEKIT_API_KEY;
        const LIVEKIT_API_SECRET = process.env.LIVEKIT_API_SECRET;

        if (!LIVEKIT_API_KEY || !LIVEKIT_API_SECRET) {
            throw new Error('LIVEKIT_API_KEY and LIVEKIT_API_SECRET environment variables are required.');
        }

        this.webhookReceiver = new WebhookReceiver(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
    }

    async createToken(roomName: string, participantName: string): Promise<string> {
        if (!roomName || !participantName) {
            throw new HttpException('roomName and participantName are required', HttpStatus.BAD_REQUEST);
        }

        const at = new AccessToken(LIVEKIT_API_KEY, LIVEKIT_API_SECRET, {identity: participantName});
        at.addGrant({roomJoin: true, room: roomName});
        return at.toJwt();
    }

}

