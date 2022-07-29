const { DefinePlugin } = require('webpack');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  entry: './src/index.tsx',
  mode: 'production',
  plugins: [
    new DefinePlugin({
      __API_URL__: JSON.stringify('main 서버 API 주소'),
    }),
  ],
});
