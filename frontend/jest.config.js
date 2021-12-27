export default {
  "transform": {
    "^.+\\.(t|j)sx?$": [
      "@swc/jest",
      {
        "sourceMaps": true,

        "jsc": {
          "parser": {
            "syntax": "typescript",
            "tsx": true
          },

          "transform": {
            "react": {
              "runtime": "automatic"
            }
          }
        }
      }
    ]
  },
  moduleFileExtensions: ['js','ts', 'tsx'],
  extensionsToTreatAsEsm: ['.ts', '.tsx'],
  testEnvironment: 'jest-environment-jsdom',
  transformIgnorePatterns: ['<rootDir>/node_modules'],
  testMatch: ['<rootDir>/src/**/__tests__/*.test.tsx'],
  setupFilesAfterEnv: ['<rootDir>/jest-setup.js',  '@testing-library/jest-dom/extend-expect'
]
}


