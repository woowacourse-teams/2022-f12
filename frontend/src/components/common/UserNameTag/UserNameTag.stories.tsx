import UserNameTag from './UserNameTag';

export default {
  component: UserNameTag,
  title: 'UserNameTag',
};

const Template = (args) => <UserNameTag {...args} />;

export const Defaults = Template.bind({});
Defaults.args = {
  username: '인도 아저씨',
};
