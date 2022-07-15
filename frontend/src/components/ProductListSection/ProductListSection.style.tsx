import { Link } from 'react-router-dom';
import styled from 'styled-components';

export const Container = styled.section`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const Title = styled.h1`
  font-size: 1.5rem;
`;

export const CustomLink = styled(Link)``;

export const Wrapper = styled.div`
  margin: 1rem auto 0 auto;
  width: 100%;
`;
