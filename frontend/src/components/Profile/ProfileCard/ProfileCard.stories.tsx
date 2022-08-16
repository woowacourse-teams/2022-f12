import { ComponentStory } from '@storybook/react';

import ProfileCard from '@/components/Profile/ProfileCard/ProfileCard';

export default {
  component: ProfileCard,
  title: 'Components/Profile/ProfileCard',
};

const Template: ComponentStory<typeof ProfileCard> = (args) => (
  <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
    <ProfileCard {...args} />
  </div>
);

const defaultArgs = {
  id: 1,
  gitHubId: 'jswith',
  imageUrl: 'https://avatars.githubusercontent.com/u/64275588?v=4',
  careerLevel: 'junior',
  jobType: 'frontend',
  profileProducts: [
    {
      id: 1,
      name: '키보드',
      imageUrl: 'https://avatars.githubusercontent.com/u/64275588?v=4',
      reviewCount: 3,
      rating: 4,
      category: 'keyboard',
    },
  ],
};

export const Default = () => <Template {...defaultArgs} />;