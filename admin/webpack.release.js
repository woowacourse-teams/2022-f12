const { DefinePlugin } = require('webpack');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'production',
  plugins: [
    new DefinePlugin({
      __API_URL__: JSON.stringify('https://dev.f12.app/api/v1'),
      __GITHUB_CLIENT_ID__: JSON.stringify('2ecae59445f2f8ffd725'),
    }),
  ],
});
