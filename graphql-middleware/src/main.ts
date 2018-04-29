const PORT = process.env.PORT || '0';
const AVPR_PREFIX = process.env.AVPR_PREFIX || '/avpr';
const AVPR_PORT = process.env.AVPR_PORT || '8000';
const AVPR_HOST = process.env.AVPR_HOST || '127.0.0.1';
const AVPR_PROTOCOL = process.env.AVPR_PROTOCOL || 'http';
import { serverFactory } from './server';
serverFactory(PORT, AVPR_HOST, AVPR_PREFIX, AVPR_PORT, AVPR_PROTOCOL);
