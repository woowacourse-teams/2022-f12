const { DefinePlugin } = require('webpack');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  entry: './src/index.tsx',
  mode: 'production',
  plugins: [
    new DefinePlugin({
      __API_URL__: JSON.stringify('https://prod.f12.app/api/v1'),
    }),
  ],
});
