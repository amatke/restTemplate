# restTemplate
Rest API (Server_ project for existing Rest template(client) project

Getting all pages:

    @GetMapping
    public String home(Model model){
        ResponseEntity<List<Page>> response = restTemplate
                .exchange("http://localhost:8080/admin/pages",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Page>>() {});

        List<Page> pages = (List<Page>) response.getBody();
        model.addAttribute("pages", pages);
        return "admin/pages/index";
    }
    
Post a page:
     
    restTemplate.postForObject("http://localhost:8080/admin/pages", page, Page.class);

Get page by id:

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Page page = restTemplate.getForObject("http://localhost:8080/admin/pages/edit/{id}", Page.class, id);
        model.addAttribute("page", page);
        return "admin/pages/edit";
    }
    
Delete page by id:

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        restTemplate.delete("http://localhost:8080/admin/pages/delete/{id}", id);
        redirectAttributes.addFlashAttribute("message", "Page deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/pages";
    }
