import ProductDetail from './ProductDetail';
import { ComponentStory } from '@storybook/react';
import sampleKeyboard from './sample_keyboard.jpg';
import styled from 'styled-components';

export default {
  component: ProductDetail,
  title: 'Components/ProductDetail',
};

const Container = styled.div`
  width: 500px;
`;

const Template: ComponentStory<typeof ProductDetail> = (args) => (
  <Container>
    <ProductDetail {...args} />
  </Container>
);

export const Defaults: ComponentStory<typeof ProductDetail> = Template.bind({});
Defaults.args = {
  productImage: sampleKeyboard,
  name: '예쁜 키보드',
  rating: 5,
};
