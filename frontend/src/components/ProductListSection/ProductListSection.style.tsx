import { Link } from 'react-router-dom';
import styled from 'styled-components';

export const Container = styled.section`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const Title = styled.h1`
  font-size: 1.2rem;
`;

export const CustomLink = styled(Link)``;

export const Wrapper = styled.div`
  display: grid;
  justify-items: center;
  grid-template-columns: repeat(5, 1fr);
  gap: 1rem;

  width: 100%;
`;
