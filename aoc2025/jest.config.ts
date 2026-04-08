import type { Config } from 'jest';

const config: Config = {
  moduleNameMapper: {'(.+)\\.js': '$1'},
  roots: ['./src']
};

export default config;
