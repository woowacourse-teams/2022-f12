import UserNameTag from '@/components/common/UserNameTag/UserNameTag';
import { ComponentStory } from '@storybook/react';

export default {
  component: UserNameTag,
  title: 'Components/UserNameTag',
};

const Template: ComponentStory<typeof UserNameTag> = (args) => (
  <UserNameTag {...args} />
);

const defaultArgs = {
  profileImage: 'https://avatars.githubusercontent.com/u/61769743?v=4',
  username: '인도 아저씨',
};

export const Default = () => <Template {...defaultArgs} />;
