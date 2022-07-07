import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 0.5rem;
  width: 12rem;
  border-radius: 0.375rem;
  padding: 1rem;
  box-shadow: 0 0.35rem 0.7rem -0.2rem rgba(0, 0, 0, 0.3);
`;

export const Image = styled.img`
  width: 100%;
  height: 8rem;
  object-fit: cover;
`;

export const Wrapper = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const Name = styled.p`
  font-size: 1rem;
`;
