import type { Config } from 'jest';

const config: Config = {
  clearMocks: true,
  testEnvironment: 'jsdom',
  moduleFileExtensions: ['tsx', 'js', 'ts', 'svg'],
  moduleNameMapper: {
    '\\.svg': '<rootDir>/src/mocks/jestSvgMock.tsx',
    '\\.(png|gif|webp)$': '<rootDir>/src/mocks/jestFileMock.ts',
    '@/(.*)': '<rootDir>/src/$1',
  },
  globals: {
    __API_URL__: 'mock/api/url',
    __GITHUB_CLIENT_ID__: 'mock/api/url',
  },
  setupFilesAfterEnv: ['<rootDir>/jest-setup.js'],

  moduleDirectories: ['node_modules', '<rootDir>/'],
};

export default config;
