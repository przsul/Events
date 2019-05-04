-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 04 Maj 2019, 14:10
-- Wersja serwera: 10.1.38-MariaDB
-- Wersja PHP: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `pwswlab05`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `events`
--

CREATE TABLE `events` (
  `id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  `agend` text COLLATE utf8mb4_polish_ci NOT NULL,
  `time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Zrzut danych tabeli `events`
--

INSERT INTO `events` (`id`, `name`, `agend`, `time`) VALUES
(1, 'Wydarzenie3', 'Jest to jakies bardzo wazne wydarzzenie', '2019-04-29 08:15:00'),
(3, 'Nowy', 'Jakieś wydarzenie', '2020-04-29 08:36:00');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `login` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  `password` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  `firstname` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  `lastname` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  `type` char(1) COLLATE utf8mb4_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`id`, `login`, `password`, `firstname`, `lastname`, `email`, `type`) VALUES
(1, 'karol', '13f611f2cf29cd2c309c2dd19670a17d', 'Karoll', 'Weynaa', 'weyna.karol@gmail.comm', 'U'),
(2, 'karol2', '13f611f2cf29cd2c309c2dd19670a17d', 'KarolA', 'WeynaA', 'admin@wp.pl', 'A'),
(10, 'Nowy', '202025f4f6b40ab1e3df52dee62b998e', 'Nowy', 'Nowy', 'Nowy', 'U'),
(11, 'Nowy', '202025f4f6b40ab1e3df52dee62b998e', 'Nowy', 'Nowy', 'Nowy', 'U'),
(12, 'karol4', 'ABC', 'Karol', 'Weynaaa', 'abc@wp.pl', 'U');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users_events`
--

CREATE TABLE `users_events` (
  `id_user` int(11) NOT NULL,
  `id_event` int(11) NOT NULL,
  `type` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  `food` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL,
  `status` varchar(100) COLLATE utf8mb4_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Zrzut danych tabeli `users_events`
--

INSERT INTO `users_events` (`id_user`, `id_event`, `type`, `food`, `status`) VALUES
(1, 1, 'Słuchacz', 'Bez preferencji', 'Potwierdzone');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `users_events`
--
ALTER TABLE `users_events`
  ADD PRIMARY KEY (`id_user`,`id_event`),
  ADD KEY `id_event` (`id_event`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `events`
--
ALTER TABLE `events`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT dla tabeli `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `users_events`
--
ALTER TABLE `users_events`
  ADD CONSTRAINT `users_events_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `users_events_ibfk_2` FOREIGN KEY (`id_event`) REFERENCES `events` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
