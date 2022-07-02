import UserNameTag from './UserNameTag';
import profileImage from '../ReviewCard/sample_profile.jpg';
import { ComponentStory } from '@storybook/react';

export default {
  component: UserNameTag,
  title: 'Components/UserNameTag',
};

const Template: ComponentStory<typeof UserNameTag> = (args) => (
  <UserNameTag {...args} />
);

export const Defaults: ComponentStory<typeof UserNameTag> = Template.bind({});
Defaults.args = {
  profileImage,
  username: '인도 아저씨',
};
