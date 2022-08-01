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
      __API_URL__: JSON.stringify('http://3.34.47.160:8080/api/v1'),
    }),
  ],
});
