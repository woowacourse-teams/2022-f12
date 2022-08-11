import { ComponentStory } from '@storybook/react';

import HeaderLogo from '@/components/common/HeaderLogo/HeaderLogo';

export default {
  component: HeaderLogo,
  title: 'Components/Header',
};

const Template: ComponentStory<typeof HeaderLogo> = () => <HeaderLogo />;

export const Default = () => <Template />;
