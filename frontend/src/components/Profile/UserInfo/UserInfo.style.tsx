import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

export const Container = styled.div`
  display: flex;
  align-items: center;
  width: max-content;
  gap: 0.6rem;
`;

export const ImageWrapper = styled.div`
  width: 4rem;
  aspect-ratio: 1 / 1;
  border-radius: 50%;
  border: 2px solid ${({ theme }) => theme.colors.gray};
  overflow: hidden;
`;

export const InfoWrapper = styled.div`
  display: flex;
  flex-direction: column;
  font-size: 1.1rem;
  gap: 0.4rem;
`;

export const UserNameWrapper = styled.div`
  display: flex;
`;

export const ProfileImage = styled.img`
  width: 100%;
  object-fit: cover;
`;

export const GitHubId = styled.a`
  text-decoration: underline;
  font-weight: 600;
`;

export const ChipWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
`;

export const FollowerCount = styled.p`
  font-size: 0.95rem;
`;

export const ButtonContainer = styled.div`
  display: flex;
  gap: 0.5rem;
`;

export const FollowerLink = styled(Link)`
  font-size: 0.95rem;
  background-color: ${({ theme }) => theme.colors.primary};
  padding: 0.3rem 0.5rem;
  width: max-content;
  border-radius: 0.3rem;
  line-height: 1;

  &:hover {
    filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.25));
  }
`;

export const FollowButton = styled.button<{ followed: boolean }>`
  padding: 0.4rem 1.4rem;
  background-color: ${({ theme }) => theme.colors.primary};
  border-radius: 0.4rem;

  background-color: ${({ theme, followed }) =>
    followed ? theme.colors.secondary : theme.colors.primary};
  margin-top: 1rem;

  &:hover {
    filter: drop-shadow(2px 2px 4px rgba(0, 0, 0, 0.25));
  }

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      font-size: 0.8rem;
    }
    @media screen and ${device.tablet} {
      font-size: 1rem;
    }
    @media screen and ${device.desktop} {
      font-size: 1.2rem;
    }
  `}
`;
