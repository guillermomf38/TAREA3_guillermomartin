-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 27-03-2026 a las 11:09:51
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `circo_guillermo`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `artista`
--

CREATE TABLE `artista` (
  `apodo` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `artista_especialidades`
--

CREATE TABLE `artista_especialidades` (
  `artista_id` bigint(20) NOT NULL,
  `especialidades` enum('ACROBACIA','EQUILIBRISMO','HUMOR','MAGIA','MALABARISMO') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `coordinacion`
--

CREATE TABLE `coordinacion` (
  `fechasenior` date DEFAULT NULL,
  `senior` bit(1) NOT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `credenciales`
--

CREATE TABLE `credenciales` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `perfil` enum('ADMIN','ARTISTA','COORDINACION') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `credenciales`
--

INSERT INTO `credenciales` (`id`, `nombre`, `password`, `perfil`) VALUES
(1, 'admin', 'admin', 'ADMIN');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `espectaculo`
--

CREATE TABLE `espectaculo` (
  `id` bigint(20) NOT NULL,
  `fechafin` date NOT NULL,
  `fechaini` date NOT NULL,
  `nombre` varchar(25) NOT NULL,
  `id_coord` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `numero`
--

CREATE TABLE `numero` (
  `id` bigint(20) NOT NULL,
  `duracion` double NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `orden` int(11) NOT NULL,
  `id_espectaculo` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `numero_artista`
--

CREATE TABLE `numero_artista` (
  `id_numero` bigint(20) NOT NULL,
  `id_artista` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `persona`
--

CREATE TABLE `persona` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `nacionalidad` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `id_credencial` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `persona`
--

INSERT INTO `persona` (`id`, `email`, `nacionalidad`, `nombre`, `id_credencial`) VALUES
(1, 'admin@circo.com', 'Admin', 'España', 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `artista`
--
ALTER TABLE `artista`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `artista_especialidades`
--
ALTER TABLE `artista_especialidades`
  ADD KEY `FKn9w8puohof2e6hkxfvvu2q5xi` (`artista_id`);

--
-- Indices de la tabla `coordinacion`
--
ALTER TABLE `coordinacion`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `credenciales`
--
ALTER TABLE `credenciales`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK7wc91kwb18d1gjenerygea9xc` (`nombre`);

--
-- Indices de la tabla `espectaculo`
--
ALTER TABLE `espectaculo`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKj0sbtxa8ch0qn1nk58qtliibw` (`nombre`),
  ADD KEY `FKflew400ecpwuh1q7f2f22rhyx` (`id_coord`);

--
-- Indices de la tabla `numero`
--
ALTER TABLE `numero`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK3xwf8rn6y5npn75sjcebvmidg` (`id_espectaculo`);

--
-- Indices de la tabla `numero_artista`
--
ALTER TABLE `numero_artista`
  ADD KEY `FK2yw06s7uxsavnoirdwx0quxcv` (`id_artista`),
  ADD KEY `FKd7ur7o5rhd2yv4qb56hopo5nh` (`id_numero`);

--
-- Indices de la tabla `persona`
--
ALTER TABLE `persona`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKbfxfxg15pmy0c1imvi6ucoeem` (`email`),
  ADD UNIQUE KEY `UKk1d6k990butc3asfcehn836ja` (`id_credencial`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `credenciales`
--
ALTER TABLE `credenciales`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `espectaculo`
--
ALTER TABLE `espectaculo`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `numero`
--
ALTER TABLE `numero`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `persona`
--
ALTER TABLE `persona`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `artista`
--
ALTER TABLE `artista`
  ADD CONSTRAINT `FKa0l85tt2s3890vhbdq0scjmia` FOREIGN KEY (`id`) REFERENCES `persona` (`id`);

--
-- Filtros para la tabla `artista_especialidades`
--
ALTER TABLE `artista_especialidades`
  ADD CONSTRAINT `FKn9w8puohof2e6hkxfvvu2q5xi` FOREIGN KEY (`artista_id`) REFERENCES `artista` (`id`);

--
-- Filtros para la tabla `coordinacion`
--
ALTER TABLE `coordinacion`
  ADD CONSTRAINT `FKqd2lfahs8fyulkcd8k297c9ky` FOREIGN KEY (`id`) REFERENCES `persona` (`id`);

--
-- Filtros para la tabla `espectaculo`
--
ALTER TABLE `espectaculo`
  ADD CONSTRAINT `FKflew400ecpwuh1q7f2f22rhyx` FOREIGN KEY (`id_coord`) REFERENCES `coordinacion` (`id`);

--
-- Filtros para la tabla `numero`
--
ALTER TABLE `numero`
  ADD CONSTRAINT `FK3xwf8rn6y5npn75sjcebvmidg` FOREIGN KEY (`id_espectaculo`) REFERENCES `espectaculo` (`id`);

--
-- Filtros para la tabla `numero_artista`
--
ALTER TABLE `numero_artista`
  ADD CONSTRAINT `FK2yw06s7uxsavnoirdwx0quxcv` FOREIGN KEY (`id_artista`) REFERENCES `artista` (`id`),
  ADD CONSTRAINT `FKd7ur7o5rhd2yv4qb56hopo5nh` FOREIGN KEY (`id_numero`) REFERENCES `numero` (`id`);

--
-- Filtros para la tabla `persona`
--
ALTER TABLE `persona`
  ADD CONSTRAINT `FKp7ui3ql656vygyyfb1osgqgmf` FOREIGN KEY (`id_credencial`) REFERENCES `credenciales` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
