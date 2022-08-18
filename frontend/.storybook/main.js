const { resolve } = require('path');
const { DefinePlugin } = require('webpack');

module.exports = {
  stories: ['../src/**/*.stories.mdx', '../src/**/*.stories.@(js|jsx|ts|tsx)'],
  addons: [
    '@storybook/addon-links',
    '@storybook/addon-essentials',
    '@storybook/addon-interactions',
  ],
  webpackFinal: async (config) => {
    const rules = config.module.rules;
    const fileLoaderRule = rules.find((rule) => rule.test.test('.svg'));
    fileLoaderRule.exclude = /\.svg$/;

    rules.push({
      test: /\.svg$/,
      use: ['@svgr/webpack'],
    });

    const alias = {
      '@': resolve(__dirname, '../src/'),
    };
    config.resolve.alias = Object.assign(config.resolve.alias, alias);
    config.plugins.push(
      new DefinePlugin({
        __API_URL__: JSON.stringify('https://dev.f12.app/api/v1'),
        __GITHUB_CLIENT_ID__: JSON.stringify('404072c5857d705db2d9'),
      })
    );
    return config;
  },
  framework: '@storybook/react',
  core: {
    builder: '@storybook/builder-webpack5',
  },
  typescript: {
    reactDocgen: 'react-docgen-typescript',
    reactDocgenTypescriptOptions: {
      shouldExtractLiteralValuesFromEnum: true,
      propFilter: (prop) =>
        prop.parent ? !/node_modules/.test(prop.parent.fileName) : true,
    },
  },
};
