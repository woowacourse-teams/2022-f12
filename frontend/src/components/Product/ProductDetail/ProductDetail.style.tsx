import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  padding: 1rem;
  gap: 1rem;
`;

export const Image = styled.img`
  width: 100%;
  object-fit: cover;
`;

export const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-end;
`;

export const Name = styled.p`
  font-size: 1.5rem;
  line-height: 1.3;
  align-self: flex-start;
`;

export const Details = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
`;
