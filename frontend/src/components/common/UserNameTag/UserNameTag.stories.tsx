import { ComponentStory } from '@storybook/react';

import UserNameTag from '@/components/common/UserNameTag/UserNameTag';

export default {
  component: UserNameTag,
  title: 'Components/Common/UserNameTag',
};

const Template: ComponentStory<typeof UserNameTag> = (args) => <UserNameTag {...args} />;

const defaultArgs = {
  imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
  username: '인도 아저씨',
};

export const Default = () => <Template {...defaultArgs} />;
