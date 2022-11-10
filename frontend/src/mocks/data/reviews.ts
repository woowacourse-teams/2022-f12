import { products } from '@/mocks/data/products';

export const InventoryReview = {
  id: 1,
  product: {
    id: 1,
    name: '키보드1',
    imageUrl:
      'http://img.danawa.com/prod_img/500000/578/350/img/16350578_1.jpg?shrink=500:500&_v=20220209090426',
    reviewCount: 2,
    rating: 2,
    category: 'keyboard',
  },
  content:
    '리뷰입니다리뷰입니다리뷰입니다리뷰입니다리뷰입니다리뷰입니다리뷰입니다리뷰입니다리뷰입니다리뷰입니다리뷰입니다리뷰입니다리뷰입니다리뷰입니다리뷰입니다리뷰입니다',
  rating: 4,
  createdAt: '2022-08-18T14:39:29.686202',
};

export const reviewsWithProduct: Review[] = [
  {
    id: 1,
    author: {
      id: 1,
      gitHubId: '사용자1',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    product: products[1],
    content: `sdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflkvsdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflkvsdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlks



    sdklfjasdfklsfndlkskaldflk
    
    
    
    
    
    
    
    
    sdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflk
    
    sdklfjasdfklsfndlkskaldflk
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    sdklfjasdfklsfndlkskaldflk
    
    
    
    sdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflk
    
    
    
    
    
    
    sdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflk
    
    
    
    
    
    
    sdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflksdklfjasdfklsfndlkskaldflk
    
    
    
    
    sdklfjasdfklsfndlkskaldflk
    
    
    sdklfjasdfklsfndlkskaldflk
    
    
    sdklfjasdfklsfndlkskaldflk
    
    
    
    
    
    
    
    
    
    
    
    
    sdklfjasdfklsfndlkskaldflk
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    `,
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 2,
    author: {
      id: 2,
      gitHubId: '사용자2',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    product: products[2],
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 3,
    author: {
      id: 3,
      gitHubId: '사용자3',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    product: products[3],
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 4,
    author: {
      id: 4,
      gitHubId: '사용자4',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    product: products[4],
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 5,
    author: {
      id: 5,
      gitHubId: '사용자5',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    product: products[5],
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 6,
    author: {
      id: 6,
      gitHubId: '사용자6',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    product: products[6],
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 7,
    author: {
      id: 7,
      gitHubId: '사용자7',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    product: products[7],
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 8,
    author: {
      id: 8,
      gitHubId: '사용자8',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    product: products[8],
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 9,
    author: {
      id: 9,
      gitHubId: '사용자9',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    product: products[1],
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 10,
    author: {
      id: 10,
      gitHubId: '사용자10',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    product: products[2],
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 11,
    author: {
      id: 11,
      gitHubId: '사용자11',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    product: products[3],
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 12,
    author: {
      id: 12,
      gitHubId: '사용자12',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    product: products[4],
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
];

export const reviewsWithOutProduct: Review[] = [
  {
    id: 1,
    author: {
      id: 1,
      gitHubId: '사용자1',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    productId: 1,
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: true,
  },
  {
    id: 2,
    author: {
      id: 2,
      gitHubId: '사용자2',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    productId: 2,
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 3,
    author: {
      id: 3,
      gitHubId: '사용자3',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    productId: 3,
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 4,
    author: {
      id: 4,
      gitHubId: '사용자4',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    productId: 4,
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 5,
    author: {
      id: 5,
      gitHubId: '사용자5',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    productId: 5,
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 6,
    author: {
      id: 6,
      gitHubId: '사용자6',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    productId: 6,
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 7,
    author: {
      id: 7,
      gitHubId: '사용자7',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    productId: 7,
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 8,
    author: {
      id: 8,
      gitHubId: '사용자8',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    productId: 8,
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 9,
    author: {
      id: 9,
      gitHubId: '사용자9',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    productId: 9,
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 10,
    author: {
      id: 10,
      gitHubId: '사용자10',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    productId: 10,
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 11,
    author: {
      id: 11,
      gitHubId: '사용자11',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    productId: 11,
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
  {
    id: 12,
    author: {
      id: 12,
      gitHubId: '사용자12',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
    },
    productId: 12,
    content:
      '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 ',
    rating: 5,
    createdAt: '2022-07-03 14:23:23',
    authorMatch: false,
  },
];
