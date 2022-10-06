const { DefinePlugin } = require('webpack');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'production',
  plugins: [
    new DefinePlugin({
      __API_URL__: JSON.stringify('https://prod.f12.app/api/v1'),
      __GITHUB_CLIENT_ID__: JSON.stringify('c45b273e7f5682e0d6d5'),
    }),
  ],
});
