import { Injectable } from '@nestjs/common';
import * as process from "process";
import {ConfigService} from "@nestjs/config";

@Injectable()
export class AppService {
  constructor(private readonly configService: ConfigService) {}


  getHello(): string {
    return this.configService.get("SECRET");
    // return process.env.SECRET;
    // return 'Hello World!';
  }
}
