import UserNameTag from './UserNameTag';
import profileImage from '../../../mocks/sample_profile.jpg';
import { ComponentStory } from '@storybook/react';

export default {
  component: UserNameTag,
  title: 'Components/UserNameTag',
};

const Template: ComponentStory<typeof UserNameTag> = (args) => (
  <UserNameTag {...args} />
);

const defaultArgs = {
  profileImage,
  username: '인도 아저씨',
};

export const Default = () => <Template {...defaultArgs} />;
