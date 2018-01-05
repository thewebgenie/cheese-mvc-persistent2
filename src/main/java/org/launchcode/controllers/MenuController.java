package org.launchcode.controllers;


import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");

        return "menu/index";
    }
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model) {
        model.addAttribute("title", "Add Menu");
        model.addAttribute(new Menu());
//        model.addAttribute("cheeseTypes", CheeseType.values());
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String addMenu(@ModelAttribute @Valid Menu menu,
                                       Errors errors,
                                       Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String viewMenu(@ModelAttribute @Valid Menu menu,
                           @PathVariable int menuId,
                           Model model) {
        Menu men = menuDao.findOne(menuId);
        model.addAttribute(men);
        return "view";
    }
    @RequestMapping(value = "add-item", method = RequestMethod.GET)
    public String addItem(@PathVariable int menuId,
                           Model model) {

//        if (errors.hasErrors()) {
//            model.addAttribute("title", "Add Menu");
//            return "menu/add";
//        }
        Menu men = menuDao.findOne(menuId);

        AddMenuItemForm form = new AddMenuItemForm(men, cheeseDao.findAll());
        model.addAttribute("form", form);
        model.addAttribute("title", "Add item to menu:"+men.getName());
        return "add-item";
    }
    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String addItem(@ModelAttribute @Valid AddMenuItemForm addMenuItemForm,
                          @PathVariable int menuId,
                          @PathVariable int cheeseId,
                          Errors errors,
                          Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Item");
            return "menu/add-item";
        }

        Cheese cheese = cheeseDao.findOne(cheeseId);
        Menu theMenu = menuDao.findOne(menuId);
        theMenu.addItem(cheese);
        menuDao.save(theMenu);

        return "redirect:view/" + theMenu.getId();
    }
}
