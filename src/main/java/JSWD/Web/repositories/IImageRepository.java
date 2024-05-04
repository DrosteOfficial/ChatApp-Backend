package JSWD.Web.repositories;

import JSWD.Web.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImageRepository extends JpaRepository<Image, Long>{};

