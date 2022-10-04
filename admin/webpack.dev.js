const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const path = require('path');
const { DefinePlugin } = require('webpack');

module.exports = merge(common, {
  mode: 'development',
  devServer: {
    static: {
      directory: path.join(__dirname, 'public'),
    },
    open: true,
    historyApiFallback: true,
    compress: true,
    port: 3000,
  },
  plugins: [
    new DefinePlugin({
      __API_URL__: JSON.stringify('https://dev.f12.app/api/v1'),
      __GITHUB_CLIENT_ID__: JSON.stringify('404072c5857d705db2d9'),
    }),
  ],
});
