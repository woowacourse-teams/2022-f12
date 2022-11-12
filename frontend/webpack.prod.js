const { DefinePlugin } = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const path = require('path');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'production',
  plugins: [
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, './public/index.html'),
    }),
    new DefinePlugin({
      __API_URL__: JSON.stringify('https://prod.f12.app/api/v1'),
      __GITHUB_CLIENT_ID__: JSON.stringify('e77cbdeefd706dcff3f0'),
    }),
  ],
});
