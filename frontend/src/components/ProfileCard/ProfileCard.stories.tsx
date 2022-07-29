import ProfileCard from '@/components/ProfileCard/ProfileCard';
import { ComponentStory } from '@storybook/react';

export default {
  component: ProfileCard,
  title: 'Components/ProfileCard',
};

const Template: ComponentStory<typeof ProfileCard> = () => (
  <div
    style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}
  >
    <ProfileCard />
  </div>
);

export const Default = () => <Template />;
