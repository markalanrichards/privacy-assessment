module.exports = {
  snapshotSerializers: ['enzyme-to-json/serializer'],
  setupFiles: ['jest-plugin-context/setup', '<rootDir>/jest.setup.ts'],
  transform: {
    '^.+\\.tsx?$': 'ts-jest'
  },
  moduleFileExtensions: ['ts', 'tsx', 'js', 'jsx', 'json', 'node'],
  transformIgnorePatterns: ['<rootDir>/node_modules'],

  testMatch: ['<rootDir>/**/*.test.ts?(x)'],
  moduleDirectories: ['node_modules'],
  globals: {
    'ts-jest': {
      enableTsDiagnostics: true
    }
  }
}
