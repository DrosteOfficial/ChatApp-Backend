package JSWD.Web.dao;

import JSWD.Web.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;

public interface IImageRepository extends JpaRepository<Image, Long>{};

