CREATE TABLE buses (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       transit_agency VARCHAR(100) NOT NULL,
                       manufacturer VARCHAR(100) NOT NULL,
                       model VARCHAR(100) NOT NULL,
                       fleet_number_start INT NOT NULL,
                       fleet_number_end INT NOT NULL
);

INSERT INTO buses (
    transit_agency,
    manufacturer,
    model,
    fleet_number_start,
    fleet_number_end
)
VALUES
    ('OC Transpo', 'Alexander Dennis', 'Enviro500 MMC', 8101, 8179),
    ('OC Transpo', 'New Flyer', 'D60LF', 6351, 6403),
    ('OC Transpo', 'New Flyer', 'D60LFR', 6404, 6709),
    ('OC Transpo', 'New Flyer', 'XD60', 6710, 6759),
    ('OC Transpo', 'New Flyer', 'XE40 Xcelsior', 2101, 2279),
    ('OC Transpo', 'Nova Bus', 'LFS', 4601, 4849),
    ('OC Transpo', 'Nova Bus', 'LFS', 4850, 4861),
    ('OC Transpo', 'Nova Bus', 'LFSe+', 2501, 2555);


