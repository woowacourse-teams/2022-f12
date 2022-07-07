import styled from 'styled-components';

export const Container = styled.article`
  display: flex;
  gap: 1rem;
  flex-direction: column;
  border-radius: 0.375rem;
  padding: 1rem;
  width: 25rem;
  height: max-content;
  box-shadow: 0 0.35rem 0.7rem -0.2rem rgba(0, 0, 0, 0.3);
`;

export const Wrapper = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const Content = styled.p`
  line-height: 1.4;
`;
