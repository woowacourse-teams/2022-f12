import ReviewCard from './ReviewCard';
import { ComponentStory } from '@storybook/react';

import sampleProfileImage from './sample_profile.jpg';

export default {
  component: ReviewCard,
  title: 'ReviewCard',
};

const Template: ComponentStory<typeof ReviewCard> = (args) => (
  <ReviewCard {...args} />
);

export const Defaults: ComponentStory<typeof ReviewCard> = Template.bind({});
Defaults.args = {
  profileImage: sampleProfileImage,
  username: '인도 아저씨',
  rating: 5,
  content:
    '무접점은 처음 사용이라 바로 적응되진 않아요 그래도 검증된 제품이라 역시 좋긴 좋네요 작업용으로 마지막 키보드라 생각한거라 비싸도 확 질렀습니다 아는 분은 아시겠지만 제품이 국내로 넘어온 후 관세청에서 문자로 제세액 3만원이상의 금액을 입금하라고 오더라구요 알고보니 수입 제한금액 오버.. 추가금을 고려해야 해요 그리고 배송은 딱 2주 걸렸어요 참고하세요^^',
};
