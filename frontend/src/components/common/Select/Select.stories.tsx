import Select from './Select';
import { ComponentStory } from '@storybook/react';
import { useState } from 'react';

export default {
  component: Select,
  title: 'Components/Select',
};

const options = [
  { value: 'default', text: '기본 순' },
  { value: 'rating', text: '평점 높은 순' },
  { value: 'reviewCount', text: '리뷰 많은 순' },
];

const Template: ComponentStory<typeof Select> = () => {
  const [value, setValue] = useState<string>(options[0].value);
  return <Select value={value} setValue={setValue} options={options} />;
};

export const Defaults: ComponentStory<typeof Select> = Template.bind(
  {}
) as ComponentStory<typeof Select>;
Defaults.args = {};
