import { ComponentStory } from '@storybook/react';

import ProfileCard from '@/components/Profile/ProfileCard/ProfileCard';

import { members } from '@/mocks/data';

export default {
  component: ProfileCard,
  title: 'Components/Profile/ProfileCard',
};

const Template: ComponentStory<typeof ProfileCard> = (args) => (
  <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
    <ProfileCard {...args} />
  </div>
);

const defaultArgs = { profileSearchResult: members[2] };

export const Default = () => <Template {...defaultArgs} />;
